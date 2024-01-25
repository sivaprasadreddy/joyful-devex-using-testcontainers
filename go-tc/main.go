package main

import (
	"flag"
	log "github.com/sirupsen/logrus"
	"github.com/sivaprasadreddy/joyful-devex-using-testcontainers/go-tc/config"
)

func main() {
	var confFile string
	flag.StringVar(&confFile, "conf", ".env", "config path, eg: -conf dev.env")
	flag.Parse()
	cfg, err := config.GetConfig(confFile)
	if err != nil {
		log.Fatal(err)
	}
	app := NewApp(cfg)
	app.Run()
}
