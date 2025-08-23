use axum::{
    routing::post,
    Router,
    Json,
    extract::Extension
};
use serde::{Deserialize, Serialize};
use std::net::SocketAddr;
use tch::{CModule, Tensor, Device};

static MODEL: &[u8] = include_bytes!("../medved-k2.pt");

#[derive(Deserialize)]
struct Entity {
    frames: Vec<Frame>
}

#[derive(Deserialize)]
struct Frame {
    x: f32,
    y: f32
}

#[derive(Serialize)]
struct ResponseModel {
    proba: f32
}

async fn infer(entity: Json<Entity>, model: Extension<CModule>) -> Json<ResponseModel> {
    let model = model.as_ref();
    let len = entity.frames.len().min(100);
    let xs: Vec<f32> = entity.frames.iter().take(len).map(|f| f.x).collect();
    let ys: Vec<f32> = entity.frames.iter().take(len).map(|f| f.y).collect();

    let mean_x = xs.iter().sum::<f32>() / len as f32;
    let mean_y = ys.iter().sum::<f32>() / len as f32;
    let std_x = (xs.iter().map(|v| (v - mean_x).powi(2)).sum::<f32>() / len as f32).sqrt();
    let std_y = (ys.iter().map(|v| (v - mean_y).powi(2)).sum::<f32>() / len as f32).sqrt();

    let dx: Vec<f32> = xs.windows(2).map(|w| (w[1] - w[0] - mean_x) / (std_x + 1e-8)).collect();
    let dy: Vec<f32> = ys.windows(2).map(|w| (w[1] - w[0] - mean_y) / (std_y + 1e-8)).collect();

    let input = Tensor::of_slice2(&[dx.as_slice(), dy.as_slice()]).unsqueeze(0).to_device(Device::Cpu);
    let output = model.forward_ts(&[input]).unwrap();
    let proba = output.sigmoid().double_value(&[0]);
    Json(ResponseModel { proba: proba as f32 })
}

#[tokio::main]
async fn main() {
    let model = CModule::load_data(MODEL).expect("Failed to load model");
    let app = Router::new()
        .route("/test", post(infer))
        .layer(Extension(model));

    let addr = SocketAddr::from(([0, 0, 0, 0], 5003));
    axum::Server::bind(&addr)
        .serve(app.into_make_service())
        .await
        .unwrap();
}
