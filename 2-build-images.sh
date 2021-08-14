#!/bin/bash

echo "$(tput setaf 2)BUILDING IMAGES...$(tput sgr 0)"
docker-compose -f docker-compose.yml build
echo