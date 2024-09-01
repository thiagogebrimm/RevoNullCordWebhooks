package org.thiagogebrim.revoNullCordWebhooks;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import org.thiagogebrim.revoNullCordWebhooks.listeners.AttackListener;

import java.io.*;
import java.nio.file.Files;
import java.util.logging.Level;

public final class RevoNullCordWebhooks extends Plugin {

    private String webhookUrl;

    @Override
    public void onEnable() {
        // Carrega a configuração
        loadConfig();

        // Verifica se a URL do webhook está configurada
        if (webhookUrl == null || webhookUrl.isEmpty()) {
            getLogger().log(Level.SEVERE, "Webhook URL não configurada! Desabilitando o plugin...");
            getProxy().getPluginManager().unregisterListeners(this);
            return;
        }

        // Cria e registra o listener para ataques
        PluginManager pluginManager = getProxy().getPluginManager();
        pluginManager.registerListener(this, new AttackListener(webhookUrl));

        getLogger().info("RevoNullCordWebhooks foi ativado.");
    }

    @Override
    public void onDisable() {
        getLogger().info("RevoNullCordWebhooks foi desativado.");
    }

    private void loadConfig() {
        // Verifica se o arquivo de configuração existe
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }

        File configFile = new File(getDataFolder(), "config.yml");

        if (!configFile.exists()) {
            try {
                // Salva a configuração padrão se o arquivo não existir
                getLogger().info("Arquivo config.yml não encontrado, criando um novo padrão!");
                saveDefaultConfig();
            } catch (Exception e) {
                getLogger().log(Level.SEVERE, "Erro ao criar config.yml padrão!", e);
            }
        }

        // Utiliza Configuration para carregar o arquivo
        try {
            Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
            webhookUrl = config.getString("webhook-url", null);

            // Adicione essa linha para verificar se o webhookUrl foi carregado corretamente
            getLogger().info("URL do webhook carregada: " + webhookUrl);

        } catch (IOException e) {
            getLogger().log(Level.SEVERE, "Erro ao carregar config.yml!", e);
        }
    }


    private void saveDefaultConfig() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }

        File file = new File(getDataFolder(), "config.yml");
        if (!file.exists()) {
            try (InputStream in = getResourceAsStream("config.yml");
                 OutputStream out = Files.newOutputStream(file.toPath())) {

                byte[] buffer = new byte[1024];
                int length;
                while ((length = in.read(buffer)) > 0) {
                    out.write(buffer, 0, length);
                }

                getLogger().info("Arquivo de configuração padrão criado com sucesso.");
            } catch (IOException e) {
                getLogger().log(Level.SEVERE, "Não foi possível salvar o arquivo config.yml padrão!", e);
            }
        }
    }

}
