#!/bin/bash

echo "$(tput setaf 2)STARTING INFRASTRUCTURE...$(tput sgr 0)"
docker-compose up
echo