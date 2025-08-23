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
    let mut dx = Vec::with_capacity(100);
    let mut dy = Vec::with_capacity(100);

    for f in &entity.frames {
        dx.push(f.x);
        dy.push(f.y);
    }

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
