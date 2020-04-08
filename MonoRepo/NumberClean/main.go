package main

import (
	"bytes"
	"fmt"
	"net"
	"strings"

	"github.com/Rule-BasedGO/utils"
)

func main() {

	fmt.Println("listening on port 22222")
	for {
		ln, err := net.Listen("tcp", ":22222")
		if err != nil {
			fmt.Println(err)
			// handle error
		}
		for {
			conn, err := ln.Accept()
			if err != nil {
				// handle error
			}
			go handleRequest(conn)
		}
	}
}

func handleRequest(conn net.Conn) {
	// Make a buffer to hold incoming data.
	buf := make([]byte, 1024)
	// Read the incoming connection into the buffer.
	_, err := conn.Read(buf)
	if err != nil {
		fmt.Println("Error reading:", err.Error())
	}
	n := bytes.Index(buf, []byte{0})
	message := string(buf[:n])
	// message2 := strings.Split(message, ":")
	fmt.Println("message received: ", message)
	cleaned := utils.WordToNum(strings.ToLower(message))
	// Close the connection when you're done with it.
	connOut, err := net.Dial("tcp", "module3learning:42070")

	if err != nil {
		fmt.Println(err)
	}
	connOut.Write([]byte(cleaned))
	connOut.Close()

	conn.Close()
}
