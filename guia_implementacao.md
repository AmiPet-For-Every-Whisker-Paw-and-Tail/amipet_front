# Guia de Implementação do AmiPet Android

Este documento contém instruções detalhadas para a implementação e execução do aplicativo AmiPet Android.

## Visão Geral

O AmiPet é um aplicativo de adoção de pets estilo Tinder, onde usuários podem deslizar para curtir ou descartar pets disponíveis para adoção. O aplicativo foi desenvolvido em Kotlin utilizando Jetpack Compose para a interface do usuário e segue o padrão de arquitetura MVVM (Model-View-ViewModel).

## Requisitos

- Android Studio Arctic Fox (2021.3.1) ou superior
- JDK 11 ou superior
- Android SDK 31 (Android 12) ou superior
- Dispositivo ou emulador Android com API 24 (Android 7.0) ou superior

## Estrutura do Projeto

O projeto segue uma arquitetura moderna e organizada:

```
app/
├── src/
│   ├── main/
│   │   ├── java/com/amipet/app/
│   │   │   ├── api/           # Serviços de API e cliente HTTP
│   │   │   ├── di/            # Injeção de dependência
│   │   │   ├── local/         # Dados locais para testes
│   │   │   ├── login/         # Tela de login
│   │   │   ├── mock/          # Serviços mock para testes
│   │   │   ├── model/         # Modelos de dados
│   │   │   ├── repository/    # Repositórios para acesso a dados
│   │   │   ├── test/          # Testes de integração
│   │   │   ├── ui/            # Componentes de UI e telas
│   │   │   │   ├── components/    # Componentes reutilizáveis
│   │   │   │   ├── navigation/    # Navegação do app
│   │   │   │   ├── screens/       # Telas principais
│   │   │   │   └── theme/         # Tema e estilos
│   │   │   ├── util/          # Utilitários
│   │   │   ├── viewmodel/     # ViewModels
│   │   │   ├── AmiPetApplication.kt  # Classe de aplicação
│   │   │   └── MainActivity.kt       # Activity principal
│   │   ├── res/               # Recursos (layouts, strings, cores, etc.)
│   │   └── AndroidManifest.xml
│   └── test/                  # Testes unitários
└── build.gradle               # Configuração de build
```

## Configuração e Execução

1. **Clone o repositório**:
   ```
   git clone <url-do-repositorio>
   ```

2. **Abra o projeto no Android Studio**:
   - Abra o Android Studio
   - Selecione "Open an existing Android Studio project"
   - Navegue até a pasta do projeto e selecione-a

3. **Sincronize o projeto com o Gradle**:
   - Clique em "Sync Project with Gradle Files" na barra de ferramentas

4. **Configure o emulador ou conecte um dispositivo**:
   - Para configurar um emulador: Tools > AVD Manager > Create Virtual Device
   - Para usar um dispositivo físico: Conecte o dispositivo via USB e habilite a depuração USB

5. **Execute o aplicativo**:
   - Selecione "app" na configuração de execução
   - Clique no botão "Run" (ou pressione Shift+F10)

## Integração com Backend

O aplicativo está configurado para funcionar com dois tipos de backend:

1. **Backend Mock (em memória)**:
   - Ativado por padrão para desenvolvimento e testes
   - Não requer configuração adicional
   - Fornece dados de exemplo para todas as funcionalidades

2. **Backend Real**:
   - Para usar o backend real, edite o arquivo `ApiClient.kt` e altere a variável `useMockApi` para `false`
   - O backend real deve estar rodando em `http://10.0.2.2:3000/api/` (localhost do emulador)
   - Para alterar a URL do backend, edite a variável `baseUrl` no mesmo arquivo

## Funcionalidades Principais

### Autenticação
- Login com email e senha
- Registro de novos usuários
- Persistência de sessão

### Home (Swipe de Pets)
- Visualização de pets disponíveis para adoção
- Swipe para direita (curtir) ou esquerda (descartar)
- Animações fluidas e feedback visual

### Matches
- Visualização de matches realizados
- Estado vazio quando não há matches

### Perfil
- Visualização e edição de informações do usuário
- Logout

## Tema e Design

O aplicativo segue um design moderno inspirado no Duolingo, com uma paleta de cores verde claro e branco. Suporta modo claro e escuro, alternando automaticamente conforme as configurações do sistema.

### Cores Principais
- Primary: #4CAF50 (Verde)
- Secondary: #8BC34A (Verde Claro)
- Background: #FFFFFF (Branco) / #121212 (Escuro)

## Testes

O aplicativo inclui testes automatizados de integração com o backend mock. Estes testes são executados automaticamente ao iniciar o aplicativo e os resultados são exibidos no Logcat.

Para visualizar os resultados dos testes:
1. Abra a janela Logcat no Android Studio
2. Filtre pelo tag "BackendIntegrationTest"

## Solução de Problemas

### Erro de compilação relacionado ao Gradle
- Verifique se o Android Studio está atualizado
- Tente limpar e reconstruir o projeto: Build > Clean Project, seguido de Build > Rebuild Project
- Verifique se o JDK está configurado corretamente: File > Project Structure > SDK Location

### Erro de conexão com o backend
- Verifique se o backend está rodando no endereço correto
- Para testes, mantenha `useMockApi = true` para usar o backend em memória

### Problemas de UI
- Verifique se o dispositivo ou emulador está rodando Android 7.0 (API 24) ou superior
- Limpe o cache do aplicativo nas configurações do dispositivo

## Contato e Suporte

Para dúvidas ou suporte, entre em contato através do email: suporte@amipet.com
