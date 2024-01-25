//go:build dev
// +build dev

package main

import (
	"context"
	"fmt"
	"github.com/sivaprasadreddy/joyful-devex-using-testcontainers/go-tc/testsupport"
	"github.com/testcontainers/testcontainers-go"
	"log"
	"os"
	"os/signal"
	"syscall"
)

func init() {
	pgContainer := testsupport.InitPostgresContainer()

	// register a graceful shutdown to stop the dependencies when the application is stopped
	// only in development mode
	var gracefulStop = make(chan os.Signal)
	signal.Notify(gracefulStop, syscall.SIGTERM)
	signal.Notify(gracefulStop, syscall.SIGINT)
	go func() {
		sig := <-gracefulStop
		fmt.Printf("caught sig: %+v\n", sig)
		err := shutdownDependencies(pgContainer.Container)
		if err != nil {
			os.Exit(1)
		}
		os.Exit(0)
	}()
}

// helper function to stop the dependencies
func shutdownDependencies(containers ...testcontainers.Container) error {
	ctx := context.Background()
	for _, c := range containers {
		err := c.Terminate(ctx)
		if err != nil {
			log.Println("Error terminating the backend dependency:", err)
			return err
		}
	}

	return nil
}
