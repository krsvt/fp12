# Lab3 

Методы:
- линейный
- лагранж

## Использование
```shell
*[lab3][~/dev/itmo/fp/empty1]$ java -jar target/lab3.jar
lab3

  -s, --step <num>  Step
  -a, --alg <num>   Algorithm is one of: 'linear', 'lagrange'

```

```shell
*[lab3][~/dev/itmo/fp/empty1]$ cat somefile.csv                 
1,1
3,3
5,5
*[lab3][~/dev/itmo/fp/empty1]$ java -jar target/lab3.jar -s 0.5 -a linear < somefile.csv > out.csv
*[lab3][~/dev/itmo/fp/empty1]$ cat somefile.csv                                                   
1,1
3,3
5,5
```
