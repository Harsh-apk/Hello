package handlers

import (
	"net/http"

	"github.com/gofiber/fiber/v2"
)

func MainHandler(c *fiber.Ctx) error {
	c.Status(http.StatusOK)
	return c.SendString("Harsh")
}
