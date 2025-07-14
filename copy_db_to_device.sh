#!/bin/bash

SOURCE_DIR="/home/garjelin/work/PET_PROJECTS/TODO_LIST"
DEST_DIR="/home/garjelin/.cache/Google/AndroidStudio2025.1.1/device-explorer/Device_1/_/data/data/com.example.todo_list/databases"

cp "$SOURCE_DIR/todo_database" "$DEST_DIR/"
cp "$SOURCE_DIR/todo_database-shm" "$DEST_DIR/"
cp "$SOURCE_DIR/todo_database-wal" "$DEST_DIR/"

echo "Database files copied back to $DEST_DIR"

