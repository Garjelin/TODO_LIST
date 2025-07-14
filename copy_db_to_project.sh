#!/bin/bash

SOURCE_DIR="/home/garjelin/.cache/Google/AndroidStudio2025.1.1/device-explorer/Device_1/_/data/data/com.example.todo_list/databases"
DEST_DIR="/home/garjelin/work/PET_PROJECTS/TODO_LIST"

cp "$SOURCE_DIR/todo_database" "$DEST_DIR/"
cp "$SOURCE_DIR/todo_database-shm" "$DEST_DIR/"
cp "$SOURCE_DIR/todo_database-wal" "$DEST_DIR/"

cd "$DEST_DIR" || exit

chmod 664 todo_database
chmod 664 todo_database-shm
chmod 664 todo_database-wal

echo "Database files copied to $DEST_DIR with write permissions"
