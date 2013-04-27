package server

import (
	"database/sql"
	"lib.kael/detail/crypto"
	"lib.kael/detail/protocol"
)

var ServerBs = DefaultServerBootStrap()

type ServerBootStrap struct{
	DbHandler 		 *sql.DB
//	CachePool *CacheHandler
	ServerParam 	 *ServerCommon
	SPipelineConfig  *ServerPipeline
	MainChannel	chan int
}

func DefaultServerBootStrap() *ServerBootStrap{
	return &ServerBootStrap{}
}

func (s *ServerBootStrap) InitWithConfigFile(name string) error{
	// parse file
	// part to be initialize
	// ServerCommon
	
	// ServerPipeline(if donot contain,it will not take effect
	//   -- PipelineCoder								     
	//   -- PipelineEncrypter
	//   -- PipelineCommonParam (tcp nodelay,keepalive)
	//   -- PipelineHeartBeatInterval
	//   -- PipelineLogicHandler
	//   -- PipelineTimeTaskHandler
	//   -- PipelineExtraPack(to be think)
	
	// DbPool mysql/postgre
	// 	 -- PoolSize
	//   -- Db name/password/database name
	
	// Cache redis/memcached

	// testing here
	s.ServerParam = &ServerCommon{}
	s.ServerParam.Af = 1
	s.ServerParam.MaxConn = 2048
	s.ServerParam.Addr = "127.0.0.1:8888"
	s.ServerParam.Tp = 1
	
	s.SPipelineConfig = &ServerPipeline{}
	s.SPipelineConfig.HeartBeatInterval = 0
	s.SPipelineConfig.KeepAlive = true;
	s.SPipelineConfig.TcpNodelay = true;
	s.SPipelineConfig.Cipher = &crypto.MiscCipher{Key:0xA55AA55A}
	s.SPipelineConfig.Proto  = &protocol.ProtoBufProtocol{}
	
	return nil
}

func (s *ServerBootStrap) Start(){
	s.ServerParam.Start()
}

func (s *ServerBootStrap) Stop(){
	s.ServerParam.Stop()
}