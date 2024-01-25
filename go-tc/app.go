package main

import (
	"context"
	"errors"
	"fmt"
	log "github.com/sirupsen/logrus"
	"github.com/sivaprasadreddy/joyful-devex-using-testcontainers/go-tc/config"
	"github.com/sivaprasadreddy/joyful-devex-using-testcontainers/go-tc/db"
	"github.com/sivaprasadreddy/joyful-devex-using-testcontainers/go-tc/products"
	"net/http"
	"os/signal"
	"syscall"
	"time"

	"github.com/gin-gonic/gin"
	"github.com/jackc/pgx/v5"
)

type App struct {
	Router *gin.Engine
	cfg    config.AppConfig
	db     *pgx.Conn
}

func NewApp(cfg config.AppConfig) *App {
	app := &App{cfg: cfg}
	app.init()
	return app
}

func (app *App) init() {
	app.db = db.GetDb(app.cfg)

	productsRepo := products.NewProductRepo(app.db)
	productController := products.NewProductController(productsRepo)

	r := gin.Default()

	apiRouter := r.Group("/api/products")
	{
		apiRouter.GET("", productController.FindAll)
		apiRouter.GET("/:id", productController.FindByID)
		apiRouter.POST("", productController.Create)
		apiRouter.PUT("/:id", productController.Update)
		apiRouter.DELETE("/:id", productController.Delete)
	}

	app.Router = r
}

func (app *App) Run() {
	// Create a context that listens for the interrupt signal from the OS.
	ctx, stop := signal.NotifyContext(context.Background(), syscall.SIGINT, syscall.SIGTERM)
	defer stop()

	port := fmt.Sprintf(":%d", app.cfg.ServerPort)
	srv := &http.Server{
		Handler:        app.Router,
		Addr:           port,
		ReadTimeout:    10 * time.Second,
		WriteTimeout:   10 * time.Second,
		MaxHeaderBytes: 1 << 20,
	}

	// Initializing the server in a goroutine so that
	// it won't block the graceful shutdown handling below
	go func() {
		if err := srv.ListenAndServe(); err != nil && !errors.Is(err, http.ErrServerClosed) {
			log.Fatalf("listen: %s", err)
		}
	}()

	// Listen for the interrupt signal.
	<-ctx.Done()

	// Restore default behavior on the interrupt signal and notify user of shutdown.
	stop()
	log.Infoln("shutting down gracefully, press Ctrl+C again to force")

	// The context is used to inform the server it has 5 seconds to finish
	// the request it is currently handling
	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()
	if err := srv.Shutdown(ctx); err != nil {
		log.Fatal("Server forced to shutdown: ", err)
	}
	log.Infoln("Server exiting")
}
