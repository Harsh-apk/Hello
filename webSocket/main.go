package main

import (
	"encoding/json"
	"fmt"
	"log"
	"webSocketImplementation/handlers"
	"webSocketImplementation/store"
	"webSocketImplementation/types"

	"github.com/gofiber/contrib/websocket"
	"github.com/gofiber/fiber/v2"
)

var dataStore = store.CreateStore()
var register = make(chan types.UserDetail)
var unregister = make(chan types.User)
var sendMessage = make(chan types.MessageDetail)

func runHub() {
	for {
		select {
		case connect := <-register:
			_, err := dataStore.GetOnlineUserConnection(types.User(connect.Name))
			if err != nil {
				//user doesn't exists
				dataStore.MakeUserOnline(connect.Name, connect.Connection)
			}
			//user already exists
			//send all pending messages here and delete them
			msgs, err := dataStore.GetPendingMessagesOfUser(types.User(connect.Name))
			var sendMessage types.MessageDetail
			sendMessage.Pending = true
			if err == nil {
				for userName, messages := range *msgs {
					for _, msg := range messages {
						sendMessage.Message = msg
						sendMessage.ReceiverName = connect.Name
						sendMessage.SenderName = userName
						connect.Connection.WriteJSON(sendMessage)
					}
				}
			}
			dataStore.DeletePendingMessagesOfUser(types.User(connect.Name))

		case connect := <-unregister:
			dataStore.DeleteOnlineUser(connect)

		case message := <-sendMessage:
			receiver, err := dataStore.GetOnlineUserConnection(message.ReceiverName)
			if err != nil {
				//receiver not online
				//make messages pending
				dataStore.InsertPendingMessageOfUser(message.ReceiverName, message.SenderName, message.Message)
			} else {
				//receiver online, directly send message

				err := receiver.WriteJSON(message)
				if err != nil {
					log.Println("write err: ", err)
					unregister <- message.ReceiverName
				}
			}
		}

	}
}

func main() {
	//initialize store
	go runHub()
	app := fiber.New()

	app.Use("/ws", func(c *fiber.Ctx) error {
		// IsWebSocketUpgrade returns true if the client
		// requested upgrade to the WebSocket protocol.
		if websocket.IsWebSocketUpgrade(c) {
			c.Locals("allowed", true)
			return c.Next()
		}
		return fiber.ErrUpgradeRequired
	})

	app.Get("/", handlers.MainHandler)
	app.Get("/ws/:id", websocket.New(func(c *websocket.Conn) {
		defer func() {
			unregister <- types.User(c.Params("id"))
		}()
		name := c.Params("id")

		register <- types.UserDetail{
			Name:       types.User(name),
			Connection: c,
		}

		for {
			_, message, err := c.ReadMessage()
			if err != nil {
				if websocket.IsUnexpectedCloseError(err, websocket.CloseGoingAway, websocket.CloseAbnormalClosure) {
					log.Println("read error:", err)
				}

				return // Calls the deferred function, i.e. closes the connection on error
			}
			//fmt.Println("message: ", string(message))
			var msg types.MessageDetail
			json.Unmarshal(message, &msg)
			msg.SenderName = types.User(name)
			msg.Pending = false
			fmt.Println(msg)
			sendMessage <- msg
			// fmt.Println("message: ", msg.Message)
			// fmt.Println("sender: ", msg.SenderName)
			// fmt.Println("receiver: ", msg.ReceiverName)
		}

	}))

	app.Listen(":3000")

}

// for {
// 	messageType, message, err := c.ReadMessage()
// 	if err != nil {
// 		if websocket.IsUnexpectedCloseError(err, websocket.CloseGoingAway, websocket.CloseAbnormalClosure) {
// 			log.Println("read error:", err)
// 		}

// 		return // Calls the deferred function, i.e. closes the connection on error
// 	}

// 	if messageType == websocket.TextMessage {
// 		// Broadcast the received message
// 		broadcast <- string(message)
// 	} else {
// 		log.Println("websocket message received of type", messageType)
// 	}
// }
