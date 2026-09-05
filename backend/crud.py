from sqlalchemy.orm import Session
from models import Product


def get_product_by_id(db: Session, id: int):
    return db.query(Product).filter(Product.id == id).first()


def create_product(db: Session, name: str, price: float):
    new_product = Product(name=name, price=price)
    db.add(new_product)
    db.commit()
    db.refresh(new_product)
    return new_product