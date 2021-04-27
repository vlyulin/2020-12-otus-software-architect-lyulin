
## ����������
https://www.baeldung.com/jpa-join-column
https://habr.com/ru/post/132385/

https://github.com/SomMeri/org.meri.jpa.tutorial/blob/master/src/main/java/org/meri/jpa/relationships/entities/bestpractice/SafePerson.java

## ��� mappedBy � �����������
��������� @JoinColumn ���������, ��� ��� �������� �������� ���������� ��������� (�� ����: ��������������� ������� ����� ������� � ������� ������ � ��������� �������), ����� ��� ������� mappedBy ���������, ��� �������� � ���� ������� �������� �������� �������� ���������, � �������� ��������� � �������� "other". ��� ����� ��������, ��� �� ������ �������� ������ � ������ ������� �� ������, ������� �� ������������ � ������� "mappedBy" (��������� ��������������� �����).

� ���������, ��� ���� � ������� ���������� ��������� ����� ��������� ��������� �������:

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

