# 🧩 Guia de Atualização de Branches com Git Rebase vs Merge

Este guia explica como atualizar sua branch de feature com as mudanças da `develop` sem alterar o histórico com `merge`, usando `git rebase`. Também mostra como atualizar a `develop` após isso, e simula os diferentes históricos gerados por cada abordagem.

---

## 🚀 Objetivo

Atualizar a branch `feature/*` com as últimas mudanças da `develop`, mantendo o histórico limpo e linear, evitando commits de merge.

---

## 📘 Etapas com `git rebase`

### 1. Acesse sua branch de feature

```bash
git checkout feature/minha-funcionalidade


```

### 2. Busque atualizações do remoto

```bash
git fetch origin

```

### 3. Rebase da develop na sua branch

```bash
git rebase origin/develop

```

### 4. Se houver conflitos
```bash 
# Resolva os conflitos nos arquivos
git add <arquivo_resolvido>
git rebase --continue

```

### 5. Atualize o repositório remoto

```bash 
git push --force-with-lease

```

## 📦 Atualizar a develop depois do rebase

> **Nunca rebase a `develop` diretamente! Use merge ou PR.**

### Opção 1: Pull Request (Recomendado)

- Crie um PR da sua branch para a `develop`
    
- Aguarde aprovação e faça o merge via interface (GitHub/GitLab)

### Opção 2: Merge Manual

```bash
git checkout develop
git pull origin develop
git merge feature/minha-funcionalidade
git push origin develop

```


## 🔄 Comparação de Histórico

### 📥 Usando `git pull` com merge

```text 
develop: A---B---C---F---G
                \       \
feature:          D---E--M

```

`M` = commit de merge (histórico mais "poluído")

### 🔃 Usando `git rebase`

```text 
develop: A---B---C---F---G
                            \
feature:                     D'--E'

```

`D'`, `E'` = commits reaplicados (histórico linear)

## 📊 Tabela Comparativa

|Aspecto|`git pull` com merge|`git rebase`|
|---|---|---|
|Histórico|Merge commits presentes|Linear e limpo|
|Clareza|Pode confundir|Fácil de entender|
|Segurança|Mais seguro|Exige atenção ao forçar push|
|Uso ideal|Branches compartilhadas|Branches de feature|
## ✅ Dicas Finais

- Prefira rebase antes de abrir um Pull Request.
    
- Nunca use rebase em branches compartilhadas como `develop` ou `main`.
    
- Use `--force-with-lease` para evitar sobrescrever trabalho remoto acidentalment


## 📚 Referências

- [Git Rebase Docs](https://git-scm.com/docs/git-rebase)
    
