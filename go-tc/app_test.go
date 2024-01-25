package main

import (
	"context"
	"encoding/json"
	log "github.com/sirupsen/logrus"
	"github.com/sivaprasadreddy/joyful-devex-using-testcontainers/go-tc/config"
	"github.com/sivaprasadreddy/joyful-devex-using-testcontainers/go-tc/products"
	"net/http"
	"net/http/httptest"
	"strings"
	"testing"

	"github.com/sivaprasadreddy/joyful-devex-using-testcontainers/go-tc/testsupport"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/suite"
)

type ControllerTestSuite struct {
	suite.Suite
	PgContainer *testsupport.PostgresContainer
	cfg         config.AppConfig
	app         *App
	router      http.Handler
	ctx         context.Context
}

func (suite *ControllerTestSuite) SetupSuite() {
	suite.ctx = context.Background()
	suite.PgContainer = testsupport.InitPostgresContainer()
	cfg, err := config.GetConfig(".env")
	if err != nil {
		log.Fatal(err)
	}
	suite.cfg = cfg

	suite.app = NewApp(suite.cfg)
	suite.router = suite.app.Router
}

func (suite *ControllerTestSuite) TearDownSuite() {
	if err := suite.PgContainer.Container.Terminate(suite.ctx); err != nil {
		log.Fatalf("error terminating postgres container: %s", err)
	}
}

func TestControllerTestSuite(t *testing.T) {
	suite.Run(t, new(ControllerTestSuite))
}

func (suite *ControllerTestSuite) TestGetAllProducts() {
	t := suite.T()
	req, _ := http.NewRequest("GET", "/api/products", nil)
	w := httptest.NewRecorder()
	suite.router.ServeHTTP(w, req)

	assert.Equal(t, http.StatusOK, w.Code)

	actualResponseJSON := w.Body.String()
	assert.NotEqual(t, "[]", actualResponseJSON)
}

func (suite *ControllerTestSuite) TestGetProductByID() {
	t := suite.T()
	req, _ := http.NewRequest(http.MethodGet, "/api/products/1", nil)
	w := httptest.NewRecorder()
	suite.router.ServeHTTP(w, req)

	assert.Equal(t, http.StatusOK, w.Code)

	var response products.Product
	err := json.NewDecoder(w.Body).Decode(&response)

	assert.Nil(t, err)
	assert.NotNil(t, response.ID)
	assert.Equal(t, "P101", response.Code)
	assert.Equal(t, "product-1", response.Name)
	assert.Equal(t, "product one", response.Description)
	assert.Equal(t, 24.50, response.Price)
}

func (suite *ControllerTestSuite) TestCreateProduct() {
	t := suite.T()
	reqBody := strings.NewReader(`
		{
			"code": "P123",
			"name": "Product 123",
			"description": "Product 123 description",
			"price": 100
		}
	`)

	req, _ := http.NewRequest(http.MethodPost, "/api/products", reqBody)
	w := httptest.NewRecorder()
	suite.router.ServeHTTP(w, req)

	assert.Equal(t, http.StatusCreated, w.Code)

	var response products.Product
	err := json.NewDecoder(w.Body).Decode(&response)

	assert.Nil(t, err)
	assert.NotNil(t, response.ID)
	assert.Equal(t, "P123", response.Code)
	assert.Equal(t, "Product 123", response.Name)
	assert.Equal(t, "Product 123 description", response.Description)
	assert.Equal(t, 100.0, response.Price)
}

func (suite *ControllerTestSuite) TestDeleteProduct() {
	t := suite.T()

	req, _ := http.NewRequest(http.MethodDelete, "/api/products/2", nil)
	w := httptest.NewRecorder()
	suite.router.ServeHTTP(w, req)

	assert.Equal(t, http.StatusOK, w.Code)
}
