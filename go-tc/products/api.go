package products

import (
	"github.com/gin-gonic/gin"
	log "github.com/sirupsen/logrus"
	"net/http"
	"strconv"
)

type ProductController struct {
	repo ProductRepository
}

func NewProductController(repository ProductRepository) *ProductController {
	return &ProductController{repo: repository}
}

func (b ProductController) FindAll(c *gin.Context) {
	log.Infoln("Fetching all products")
	ctx := c.Request.Context()
	products, err := b.repo.FindAll(ctx)
	if err != nil {
		log.Errorln("Error while fetching products")
		c.AbortWithStatusJSON(http.StatusInternalServerError, gin.H{
			"error": "Unable to fetch products",
		})
		return
	}
	if products == nil {
		products = []Product{}
	}
	c.JSON(http.StatusOK, products)
}

func (b ProductController) FindByID(c *gin.Context) {
	id, err := strconv.Atoi(c.Param("id"))
	if err != nil {
		log.Errorf("Error while parsing productID: %v", err)
		c.AbortWithStatusJSON(http.StatusBadRequest, gin.H{
			"error": "Invalid product id",
		})
		return
	}
	log.Infof("Fetching product by id %d", id)
	ctx := c.Request.Context()
	product, err := b.repo.FindByID(ctx, id)
	if err != nil {
		log.Errorf("Error while fetching product by id: %v", err)
		c.AbortWithStatusJSON(http.StatusInternalServerError, gin.H{
			"error": "Unable to fetch product by id",
		})
		return
	}
	c.JSON(http.StatusOK, product)
}

func (b ProductController) Create(c *gin.Context) {
	log.Infoln("create product")
	ctx := c.Request.Context()
	var cp CreateProductModel
	if err := c.ShouldBindJSON(&cp); err != nil {
		c.AbortWithStatusJSON(http.StatusBadRequest, gin.H{
			"error": "Unable to parse request body. Error: " + err.Error(),
		})
		return
	}
	product := Product{
		Code:        cp.Code,
		Name:        cp.Name,
		Description: cp.Description,
		Price:       cp.Price,
	}
	product, err := b.repo.Create(ctx, product)
	if err != nil {
		log.Errorf("Error while create product %v", err)
		c.AbortWithStatusJSON(http.StatusInternalServerError, gin.H{
			"error": "Unable to create product",
		})
		return
	}
	c.JSON(http.StatusCreated, product)
}

func (b ProductController) Update(c *gin.Context) {
	id, err := strconv.Atoi(c.Param("id"))
	if err != nil {
		log.Errorf("Error while parsing productID: %v", err)
		c.AbortWithStatusJSON(http.StatusBadRequest, gin.H{
			"error": "Invalid product id",
		})
		return
	}
	log.Infof("update product id=%d", id)
	ctx := c.Request.Context()
	var upm UpdateProductModel
	if err := c.ShouldBindJSON(&upm); err != nil {
		c.AbortWithStatusJSON(http.StatusBadRequest, gin.H{
			"error": "Unable to parse request body. Error: " + err.Error(),
		})
		return
	}
	product := Product{
		ID:          id,
		Code:        upm.Code,
		Name:        upm.Name,
		Description: upm.Description,
		Price:       upm.Price,
	}
	_, err = b.repo.Update(ctx, product)
	if err != nil {
		log.Errorf("Error while update product: %v", err)
		c.AbortWithStatusJSON(http.StatusBadRequest, gin.H{
			"error": "Unable to update product",
		})
		return
	}
	product, _ = b.repo.FindByID(c.Request.Context(), id)
	c.JSON(http.StatusOK, product)
}

func (b ProductController) Delete(c *gin.Context) {
	id, err := strconv.Atoi(c.Param("id"))
	if err != nil {
		log.Errorf("Error while parsing productID: %v", err)
		c.AbortWithStatusJSON(http.StatusBadRequest, gin.H{
			"error": "Invalid product id",
		})
		return
	}
	log.Infof("delete product with id=%d", id)
	ctx := c.Request.Context()
	err = b.repo.Delete(ctx, id)
	if err != nil {
		log.Errorf("Error while deleting product: %v", err)
		c.AbortWithStatusJSON(http.StatusInternalServerError, gin.H{
			"error": "Unable to delete product",
		})
		return
	}
	c.JSON(http.StatusOK, nil)
}
