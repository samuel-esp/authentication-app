#!/bin/bash

echo "$(tput setaf 2)CREATING VOLUME...$(tput sgr 0)";
docker volume create --name=authdb
echo