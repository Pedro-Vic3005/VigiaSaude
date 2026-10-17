# Verificação da entrega

## O que foi possível verificar no ambiente de geração

- Pacote `modelobase(1).zip` inspecionado; estrutura Gradle, wrapper e dependências preservados.
- Arquivos JSON/YAML/SQL anteriores copiados para `docs/`.
- Classes de domínio Kotlin compiladas com compilador Kotlin/JVM local.
- Caminhos de fluxo e estados conferidos com o SQL e o guia de design anterior.
- Recursos XML e arquivos de texto verificados estruturalmente.

## Limitação de execução

Não foi possível produzir nem executar APK no ambiente desta conversa: não há Android SDK e o wrapper Gradle não conseguiu acessar `services.gradle.org` (`UnknownHostException`). Esta entrega inclui **o código fonte completo do MVP**, não um APK previamente compilado. A construção efetiva deve ser feita no Android Studio com SDK e dependências instaladas.

## Checklist no Android Studio

- [ ] **Sync Project with Gradle Files** finaliza sem erro
- [ ] **Build > Make Project** compila
- [ ] Testes JUnit rodam
- [ ] Tela inicial não corta texto em dispositivo de 320dp
- [ ] Registro em três passos guarda descrição, bairro e foto opcional
- [ ] Rascunho persiste ao reiniciar o app
- [ ] Modo offline de demonstração preserva o registro
- [ ] Sincronização simulada gera somente ID `DEMO-`
- [ ] Painel do agente avança estados e não permite pular transições
- [ ] Percepção comunitária só aparece após status `concluida`
- [ ] Visão de regiões nunca mostra foto e coordenada particular
- [ ] Leitor de tela lê botões e categorias com rótulos
