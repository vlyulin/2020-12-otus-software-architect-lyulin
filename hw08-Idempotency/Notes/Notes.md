
## Ассоциации
https://www.baeldung.com/jpa-join-column
https://habr.com/ru/post/132385/

https://github.com/SomMeri/org.meri.jpa.tutorial/blob/master/src/main/java/org/meri/jpa/relationships/entities/bestpractice/SafePerson.java

## Про mappedBy в ассоциациях
Аннотация @JoinColumn указывает, что эта сущность является владельцем отношения (то есть: соответствующая таблица имеет столбец с внешним ключом к указанной таблице), тогда как атрибут mappedBy указывает, что сущность в этой стороне является обратной стороной отношения, а владелец находится в сущности "other". Это также означает, что вы можете получить доступ к другой таблице из класса, который вы аннотировали с помощью "mappedBy" (полностью двунаправленная связь).

В частности, для кода в вопросе правильные аннотации будут выглядеть следующим образом:

@Entity
public class Company {
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "company")
    private List<Branch> branches;
}

@Entity
public class Branch {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "companyId")
    private Company company;
}

