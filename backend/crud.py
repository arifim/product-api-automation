from sqlalchemy.orm import Session
from models import Product
from sqlalchemy.exc import IntegrityError
from fastapi import HTTPException


def get_product_by_id(db: Session, id: int):
    return db.query(Product).filter(Product.id == id).first()


def create_product(db: Session, name: str, price: float):
    new_product = Product(name=name, price=price)
    db.add(new_product)
    try:
        db.commit()
    except IntegrityError:
        db.rollback()
        raise HTTPException(
            status_code=409,
            detail="Product with this name already exist"
        )
    db.refresh(new_product)
    return new_product


def get_all_products(db: Session):
    return db.query(Product).all()