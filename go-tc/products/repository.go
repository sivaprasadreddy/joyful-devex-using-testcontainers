package products

import (
	"context"
	"github.com/jackc/pgx/v5"
	log "github.com/sirupsen/logrus"
)

type ProductRepository interface {
	FindAll(ctx context.Context) ([]Product, error)
	FindByID(ctx context.Context, productID int) (Product, error)
	Create(ctx context.Context, product Product) (Product, error)
	Update(ctx context.Context, product Product) (Product, error)
	Delete(ctx context.Context, productID int) error
}

type productRepo struct {
	db *pgx.Conn
}

func NewProductRepo(db *pgx.Conn) ProductRepository {
	return &productRepo{db: db}
}

func (repo *productRepo) FindAll(ctx context.Context) ([]Product, error) {
	sql := "SELECT id, code, name, description, price FROM products"
	rows, err := repo.db.Query(ctx, sql)
	if err != nil {
		return nil, err
	}
	var products []Product
	defer rows.Close()
	for rows.Next() {
		var p = Product{}
		err = rows.Scan(&p.ID, &p.Code, &p.Name, &p.Description, &p.Price)
		if err != nil {
			return nil, err
		}
		products = append(products, p)
	}
	return products, nil
}

func (repo *productRepo) FindByID(ctx context.Context, id int) (Product, error) {
	log.Infof("Fetching product with id=%d", id)
	var p = Product{}
	sql := "select id, code, name, description, price FROM products where id=$1"
	err := repo.db.QueryRow(ctx, sql, id).Scan(
		&p.ID, &p.Code, &p.Name, &p.Description, &p.Price)
	if err != nil {
		return Product{}, err
	}
	return p, nil
}

func (repo *productRepo) Create(ctx context.Context, p Product) (Product, error) {
	var lastInsertID int
	sql := "insert into products(code, name, description, price) values($1, $2, $3, $4) RETURNING id"
	err := repo.db.QueryRow(ctx, sql, p.Code, p.Name, p.Description, p.Price).
		Scan(&lastInsertID)
	if err != nil {
		log.Errorf("Error while inserting product row: %v", err)
		return Product{}, err
	}
	p.ID = lastInsertID
	return p, nil
}

func (repo *productRepo) Update(ctx context.Context, p Product) (Product, error) {
	sql := "update products set code = $1, name=$2, description=$3, price=$4 where id=$5"
	_, err := repo.db.Exec(ctx, sql, p.Code, p.Name, p.Description, p.Price, p.ID)
	if err != nil {
		return Product{}, err
	}
	return p, nil
}

func (repo *productRepo) Delete(ctx context.Context, id int) error {
	sql := "delete from products where id=$1"
	_, err := repo.db.Exec(ctx, sql, id)
	return err
}
