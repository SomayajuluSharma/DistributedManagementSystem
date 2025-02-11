# Distributed File System

A basic distributed file system implementation inspired by large-scale architectures like Facebook's News Feed. This project is a learning exercise that explores core concepts such as data chunking, replication, and fault tolerance in a distributed environment.

## Table of Contents

- [Introduction](#introduction)
- [Features](#features)
- [Architecture](#architecture)
- [Technologies Used](#technologies-used)
- [Usage](#usage)
- [License](#license)
- [Contact](#contact)

## Introduction

This project demonstrates a simple distributed file system designed to split large files into manageable chunks, replicate data across nodes, and provide secure access. 

## Features

- **Data Chunking & Partitioning:** Files are divided into smaller chunks for efficient storage and retrieval.
- **Replication & Fault Tolerance:** Data is replicated across nodes to enhance reliability.
- **Efficient Indexing & Metadata Management:** Uses a NoSQL approach to quickly manage file metadata.
- **Object Storage Integration:** High-performance storage using MinIO.
- **Caching:** Utilizes caching (via Redis) to speed up data access.
- **Security:** Secured endpoints with Spring Boot Security for robust authentication and authorization.

## Architecture

The system is organized into three primary layers:
- **Client Layer:** API clients that interact with the system.
- **Application Layer:** Spring Boot services manage file operations, including splitting, storing, and retrieving file chunks.
- **Data Layer:** 
  - **MongoDB** handles file metadata.
  - **MinIO** provides the object storage for the file chunks.
  - **Redis** caches frequently accessed data to improve performance.

A high-level flow of the system:
1. A file is uploaded and broken into chunks.
2. Metadata is stored in MongoDB.
3. Chunks are stored in MinIO.
4. Redis caches popular files for faster retrieval.
5. Secure endpoints ensure only authenticated users can access the data.

## Technologies Used

- **Java & Spring Boot:** Core framework for building the backend services.
- **MongoDB:** NoSQL database for storing metadata.
- **MinIO:** Object storage for handling file chunks.
- **Redis:** Caching solution to enhance performance.
- **Spring Boot Security:** Manages authentication and authorization.

## Usage

While this README focuses on explaining the project's design and features, the application is structured to handle file uploads, downloads, and metadata retrieval via RESTful API endpoints. The endpoints include:
- **POST /api/files/upload:** To upload and process a file.
- **GET /api/files/{id}:** To retrieve file metadata.
- **GET /api/files/download/{id}:** To download a file.
- **DELETE /api/files/{id}:** To delete a file.

Feel free to explore the code for more details on how these operations are implemented.

## Contact

For questions, suggestions, or collaborations, please reach out:

- **Email:** chsomayajulusharma@gmail.com
- **LinkedIn:** https://www.linkedin.com/in/somayajulu-sharma-b30b3b18b/
