package br.com.dio.desafio.dominio;

import java.util.*;
import java.util.stream.Collectors;

public class Dev {
    private String nome;
    private final Map<String, LinkedHashSet<Conteudo>> conteudosInscritosPorBootcamp = new LinkedHashMap<>();
    private Set<Conteudo> conteudosConcluidos = new LinkedHashSet<>();

    public void inscreverBootcamp(Bootcamp bootcamp){
        this.conteudosInscritosPorBootcamp.put(bootcamp.getNome(), new LinkedHashSet<>(bootcamp.getConteudos()));
        bootcamp.getDevsInscritos().add(this);
    }

    public void progredir(Bootcamp bootcamp) {
        Optional<Conteudo> conteudo = this.conteudosInscritosPorBootcamp.get(bootcamp.getNome()).stream().findFirst();
        if(conteudo.isPresent()) {
            this.conteudosConcluidos.add(conteudo.get());
            this.conteudosInscritosPorBootcamp.values().forEach(set -> set.remove(conteudo.get()));
        } else {
            System.err.println("Você não está matriculado em nenhum conteúdo nesse bootcamp!");
        }
    }

    public double progresso(Bootcamp bootcamp) {
        Set<Conteudo> conteudosInscritos = this.conteudosInscritosPorBootcamp.get(bootcamp.getNome());
        if (conteudosInscritos == null || conteudosInscritos.isEmpty()) {
            return 100d;
        }
        int totalConteudos = bootcamp.getConteudos().size();
        return (double) (totalConteudos - conteudosInscritos.size()) / totalConteudos * 100;
    }

    public double calcularTotalXp() {
        Iterator<Conteudo> iterator = this.conteudosConcluidos.iterator();
        double soma = 0;
        while(iterator.hasNext()){
            double next = iterator.next().calcularXp();
            soma += next;
        }
        return soma;

        /*return this.conteudosConcluidos
                .stream()
                .mapToDouble(Conteudo::calcularXp)
                .sum();*/
    }


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Set<Conteudo> getConteudosInscritos() {
        Set<Conteudo> conteudosInscritos = new LinkedHashSet<>();
        this.conteudosInscritosPorBootcamp.values().forEach(conteudosInscritos::addAll);
        if (conteudosInscritos.isEmpty()) {
            System.err.println("Você não está matriculado em nenhum conteúdo!");
        }
        return conteudosInscritos;
    }

    // inscrição em bootcamp já adiciona os conteúdos, então não é necessário um setter para isso
    // public void setConteudosInscritos(Set<Conteudo> conteudosInscritos) {
    //     this.conteudosInscritos = conteudosInscritos;
    // }

    public Set<Conteudo> getConteudosConcluidos() {
        return conteudosConcluidos;
    }

    public void setConteudosConcluidos(Set<Conteudo> conteudosConcluidos) {
        this.conteudosConcluidos = conteudosConcluidos;
    }

    public Set<String> getBootcampsInscritos() {
        return this.conteudosInscritosPorBootcamp.keySet();
    }

    public Set<String> getBootcampsConcluidos() { 
        return this.conteudosInscritosPorBootcamp.keySet().stream()
                .filter(bootcamp -> this.conteudosInscritosPorBootcamp.get(bootcamp).isEmpty())
                .collect(Collectors.toSet());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof Dev dev)) return false;
        return Objects.equals(nome, dev.nome) && Objects.equals(getConteudosInscritos(), dev.getConteudosInscritos()) && Objects.equals(conteudosConcluidos, dev.conteudosConcluidos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, getConteudosInscritos(), conteudosConcluidos);
    }
}
