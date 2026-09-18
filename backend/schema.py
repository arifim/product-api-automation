from pydantic import BaseModel, Field

class ProductCreate(BaseModel):
    name: str = Field(min_length=2) #Name should have more than 2 characters
    price: float = Field(gt=0)      #Price should be greater than 0


class ProductResponse(BaseModel):
    id: int
    name: str
    price: float

    class Config:
        from_attributes = True   # разрешает собрать схему из ORM-объекта