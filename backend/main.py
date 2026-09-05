from fastapi import FastAPI, Depends, HTTPException
from sqlalchemy.orm import Session
from database import SessionLocal, engine
from models import Base, Product
from crud import get_product_by_id, create_product
from schema import ProductCreate

app = FastAPI()

# Create tables in the database
Base.metadata.create_all(bind=engine)

# Dependency to get DB session
def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()

@app.post("/product/")
async def create_product_endpoint(product: ProductCreate, db: Session = Depends(get_db)):
    return create_product(db, product.name, product.price)

@app.get("/product/{id}")
async def get_item_endpoint(id: int, db: Session = Depends(get_db)):
    product = get_product_by_id(db, id)
    if product is None:
        raise HTTPException(status_code=404, detail="Product not found")
    return product

@app.put("/product/{id}")
async def update_product_endpoint(id: int, name: str, price: float, db: Session = Depends(get_db)):
    product = db.query(Product).filter(Product.id == id).first()
    if product is None:
        raise HTTPException(status_code=404, detail="Product not found")
    
    product.name = name
    product.price = price
    db.commit()
    db.refresh(product)
    
    return product

@app.delete("/product/{id}")
async def delete_item_endpoint(id: int, db: Session = Depends(get_db)):
    product = db.query(Product).filter(Product.id == id).first()
    if product is None:
        raise HTTPException(status_code=404, detail="Product not found")
    
    db.delete(product)
    db.commit()
    
    return {"detail": "Product deleted"}