package it.einjojo.akani.core.network;

import com.google.common.collect.ImmutableList;
import it.einjojo.akani.core.InternalAkaniCore;
import it.einjojo.akani.core.api.messaging.BrokerService;
import it.einjojo.akani.core.api.messaging.ChannelMessage;
import it.einjojo.akani.core.api.messaging.ChannelReceiver;
import it.einjojo.akani.core.api.messaging.MessageProcessor;
import it.einjojo.akani.core.api.network.Group;
import it.einjojo.akani.core.api.network.NetworkManager;
import it.einjojo.akani.core.api.network.Server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

//TODO
public class CommonNetworkManager implements NetworkManager, MessageProcessor {
    private final static String UPDATE_MESSAGE_ID = "server_update";
    private static final String SERVERS_PREFIX = "servers:";
    private final Map<String, Group> groups = new HashMap<>();
    private final Map<String, Server> servers = new HashMap<>();
    private final InternalAkaniCore core;

    public CommonNetworkManager(BrokerService service, InternalAkaniCore core) {
        this.core = core;
        service.registerMessageProcessor(this);
    }

    public void load() {
        groups.clear();
        servers.clear();
        try (var jedis = core.jedisPool().getResource()) {
            var serverKeys = jedis.keys(SERVERS_PREFIX + "*");
            for (var key : serverKeys) {
                String serverName = key.substring(SERVERS_PREFIX.length());
                String groupName = jedis.get(key);
                var server = new CommonServer(core, serverName, groupName);
                servers.put(serverName, server);
                // Load groups
                var optionalGroup = group(groupName);
                if (optionalGroup.isPresent()) {
                    ((CommonGroup) optionalGroup.get()).addServer(serverName);
                } else {
                    var group = new CommonGroup(groupName, core);
                    groups.put(groupName, group);
                    group.addServer(serverName);
                }
            }
            core.logger().info("Loaded " + servers.size() + " servers and " + groups.size() + " groups");
        }

    }

    @Override
    public Optional<Server> server(String name) {
        return Optional.ofNullable(servers.get(name));
    }

    @Override
    public List<Server> servers() {
        return ImmutableList.copyOf(servers.values());
    }

    @Override
    public Optional<Group> group(String name) {
        return Optional.ofNullable(groups.get(name));
    }

    @Override
    public List<String> groups() {
        return ImmutableList.copyOf(groups.keySet());
    }

    public void register(Server server) {
        try (var jedis = core.jedisPool().getResource()) {
            jedis.set(SERVERS_PREFIX + server.name(), server.groupName());
        }
        publishUpdate();
    }

    @Override
    public void unregister(Server server) {
        try (var jedis = core.jedisPool().getResource()) {
            jedis.del(SERVERS_PREFIX + server.name());
        }
        publishUpdate();
    }

    public void publishUpdate() {
        core.brokerService().publish(ChannelMessage.builder().recipient(ChannelReceiver.all()).messageTypeID(UPDATE_MESSAGE_ID).build());
    }

    @Override
    public void processMessage(ChannelMessage message) {
        if (message.messageTypeID().equals(UPDATE_MESSAGE_ID)) {
            if (core.shuttingDown()) return;
            load();
        }
    }
}
