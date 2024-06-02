package types

import "github.com/gofiber/contrib/websocket"

type User string
type PendingMessageFromSenders map[User][]string
type UsersWithPendingMessages map[User]PendingMessageFromSenders
type OnlineUsers map[User]*websocket.Conn

type UserDetail struct {
	Name       User
	Connection *websocket.Conn
}

type MessageDetail struct {
	SenderName   User   `json:"senderName"`
	ReceiverName User   `json:"receiverName"`
	Message      string `json:"message"`
	Pending      bool   `json:"pending"`
}
