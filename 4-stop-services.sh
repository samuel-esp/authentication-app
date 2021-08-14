#!/bin/bash

echo "$(tput setaf 2)STOPPING INFRASTRUCTURES...$(tput sgr 0)"
docker-compose down -v
echo