package db

import (
	"context"
	"errors"
	"fmt"
	log "github.com/sirupsen/logrus"
	"github.com/sivaprasadreddy/joyful-devex-using-testcontainers/go-tc/config"

	"github.com/golang-migrate/migrate/v4"
	_ "github.com/golang-migrate/migrate/v4/database/postgres"
	_ "github.com/golang-migrate/migrate/v4/source/file"
	"github.com/jackc/pgx/v5"
)

func GetDb(config config.AppConfig) *pgx.Conn {
	connStr := fmt.Sprintf("host=%s port=%d user=%s password=%s dbname=%s sslmode=disable",
		config.DbHost, config.DbPort, config.DbUserName, config.DbPassword, config.DbDatabase)
	conn, err := pgx.Connect(context.Background(), connStr)
	if err != nil {
		log.Fatal(err)
	}
	if config.DbRunMigrations {
		runMigrations(config)
	}
	return conn
}

func runMigrations(config config.AppConfig) {
	sourceURL := config.DbMigrationsLocation
	databaseURL := fmt.Sprintf("postgres://%s:%s@%s:%d/%s?sslmode=disable",
		config.DbUserName, config.DbPassword, config.DbHost, config.DbPort, config.DbDatabase)
	log.Infof("DB Migration sourceURL: %s", sourceURL)
	log.Infof("DB Migration URL: %s", databaseURL)
	m, err := migrate.New(sourceURL, databaseURL)
	if err != nil {
		log.Fatalf("Database migration error: %v", err)
	}
	if err := m.Up(); err != nil && !errors.Is(err, migrate.ErrNoChange) {
		log.Fatalf("Databse migrate.up() error: %v", err)
	}
	log.Infoln("Database migration completed")
}
