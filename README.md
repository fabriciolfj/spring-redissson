# spring-redissson
-ttl -> tempo de vida do valor no cache, utilizamos o RMapCacheReactive

# Redis com spring webflux

## Bibliotecas java redis

### Jedis
- Rapido
- Não escalavel e não thread safe

### Lettuce
- Escalável
- Suporte reactive streams

### Redisson
- Escalável
- Suporte reactive streams
- Melhor abstração
- Mais recursos

### Cache local com cache remoto
- podemos ter o cache local na aplicação e o cache remoto, quando o cache local e modificado, este e sincronizado com o remoto e outra aplicação terá o valor do cache atualizado
- para isso temos algumas estratégias, como UPDATE (que execute o processo acima), NONE(que não executa).
- temos tambem a estratégia de reconectação, onde podemos manter o cache local (none) ou limpar (clear), quando perdermos a conexão com o servidor redis

### Execute redis-cli no docker
```
docker exec -it redis bash
redis-cli
keys *
```

### Comandos básicos redis-cli
- set a 1 > chave a valor 1
- rpus users 1 2 3 > chave e uma lista
- keys * > mostrar todas as chaves dos dados do usuário corrente
- flushdb > limpar a base


### Segurança redis
- dentro do redis-cli, podemos:
  - acl deluser default : excluir o usuário default
  - acl nome_do_usuario : para mostrar o usuario
  - acl list : listas os usuários
  - acl setuser nome_do_usuario >password on (para deixar habilitado)
  - acl setuser nome_do_usuario on (ativar o usuário)
  - acl setuser nome_do_usuario off (desativar o usuário)
  - logar com usuário: auth nome password
  - acl whoami : mostrar usuário corrente
  - auth defaut nopass : logando com usuário default sem password
  - para dar todas as permissões ao usuário: acl setuser sam >pass123 on allcommands allkeys
  - exigir password de todos os usuários: config set requirepass pass1234
  - permitir usuários sem password: config set requirepass ""


# Configuração cluster
````
@Configuration
public class RedisConfiguration {

	final RedisProperties properties;

	public RedisConfiguration(RedisProperties properties) {
		this.properties = properties;
	}

	@Bean
	public LettuceConnectionFactory redisConnectionFactory() {
		LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
		                                                                    .readFrom(ReadFrom.REPLICA_PREFERRED)
		                                                                    .build();
		RedisStaticMasterReplicaConfiguration staticMasterReplicaConfiguration = new RedisStaticMasterReplicaConfiguration(properties.getMaster().getHost(),
		                                                                                                                   properties.getMaster().getPort());
		properties.getSlaves().forEach(slave -> staticMasterReplicaConfiguration.addNode(slave.getHost(), slave.getPort()));
		return new LettuceConnectionFactory(staticMasterReplicaConfiguration, clientConfig);
	}

}
