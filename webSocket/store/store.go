package store

import (
	"fmt"
	"webSocketImplementation/types"

	"github.com/gofiber/contrib/websocket"
)

type MainStore struct {
	OnlineUsers              *types.OnlineUsers
	UsersWithPendingMessages *types.UsersWithPendingMessages
}
type Store interface {
	GetOnlineUserConnection(types.User) (*websocket.Conn, error)
	MakeUserOnline(types.User, *websocket.Conn)
	DeleteOnlineUser(types.User)
	GetPendingMessagesOfUser(types.User) (*types.PendingMessageFromSenders, error)
	InsertPendingMessageOfUser(offlineUser types.User, fromUser types.User, message string)
	DeletePendingMessagesOfUser(offlineUser types.User)
}

func CreateStore() Store {
	return &MainStore{
		OnlineUsers:              &(types.OnlineUsers{}),
		UsersWithPendingMessages: &(types.UsersWithPendingMessages{}),
	}
}

func (s *MainStore) GetOnlineUserConnection(user types.User) (*websocket.Conn, error) {
	conn, ok := (*s.OnlineUsers)[user]
	if !ok {
		return nil, fmt.Errorf("this user is not online")
	}
	return conn, nil
}

func (s *MainStore) MakeUserOnline(user types.User, conn *websocket.Conn) {
	(*s.OnlineUsers)[user] = conn
}

func (s *MainStore) DeleteOnlineUser(user types.User) {
	(*s.OnlineUsers)[user].Conn.Close()
	delete((*s.OnlineUsers), user)
}

func (s *MainStore) GetPendingMessagesOfUser(user types.User) (*types.PendingMessageFromSenders, error) {
	messages, ok := (*s.UsersWithPendingMessages)[user]
	if !ok {
		return nil, fmt.Errorf("no pending messages")
	}
	return &messages, nil
}

func (s *MainStore) InsertPendingMessageOfUser(offlineUser types.User, fromUser types.User, message string) {
	if (*s.UsersWithPendingMessages)[offlineUser] == nil {
		(*s.UsersWithPendingMessages)[offlineUser] = types.PendingMessageFromSenders{}

	}
	if (*s.UsersWithPendingMessages)[offlineUser][fromUser] == nil {
		(*s.UsersWithPendingMessages)[offlineUser][fromUser] = []string{}
	}

	(*s.UsersWithPendingMessages)[offlineUser][fromUser] = append((*s.UsersWithPendingMessages)[offlineUser][fromUser], message)
}

func (s *MainStore) DeletePendingMessagesOfUser(offlineUser types.User) {
	delete((*s.UsersWithPendingMessages), offlineUser)
}
