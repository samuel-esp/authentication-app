#!/bin/bash

echo "$(tput setaf 2)REMOVING IMAGES...$(tput sgr 0)"
docker image rm $(docker image ls | grep auth-app | awk '{print $3}')
echo