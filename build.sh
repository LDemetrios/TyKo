
set -x

cd typst-shared-library
export OPENSSL_DIR=$(brew --prefix openssl)
cargo build --target wasm32-wasip1 --release || exit
cp target/wasm32-wasip1/release/typst_shared.wasm ../drivers/chicory/src/main/resources/typst-shared.wasm
cd ..

wasm-tools print drivers/chicory/src/main/resources/typst-shared.wasm > lib.wat