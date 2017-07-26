#!/bin/sh
# Build script
# set -o errexit

ansible-playbook --version
ANSIBLE_FORCE_COLOR=true ansible-playbook -i ansible/inventory/$ENV ansible/es.yml --tags "log_es_restore" -v --vault-password-file /run/secrets/vault-pass
