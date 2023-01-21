# README TESTING

## 1 - Hierarchical Roles

### Sample Hierarchy - Graph

```
┌───────────────────────────────────────────────┐                               
│A1                                             │                               
└△─────────────────────────────────────────────△┘                               
┌┴───────────────────────────────────────────┐┌┴─────────────────┐              
│B2A1                                        ││B1A1              │              
└△──────────────────────△───────────────────△┘└△────────────────△┘              
┌┴────────────────────┐┌┴─────────────────┐┌┴──┴──────────────┐┌┴───────┐       
│C4B2A1               ││C3B2A1            ││C2B1A1            ││C1B1A1  │       
└△─────────△─────────△┘└△──────△─────────△┘└△──────△─────────△┘└△──────△┘       
┌┴───────┐┌┴───────┐┌┴──┴────┐┌┴───────┐┌┴──┴────┐┌┴───────┐┌┴──┴────┐┌┴───────┐
│D8C4B2A1││D7C4B2A1││D6C3B2A1││D5C3B2A1││D4C2B1A1││D3C2B1A1││D2C1B1A1││D1C1B1A1│
└────────┘└────────┘└────────┘└────────┘└────────┘└────────┘└────────┘└────────┘
```

- In a top-down role hierarchy, privilege increases as we descend downward.
- e.g. Assignees with role *D7C4B2A1* inherits all that are above: [ C4B2A1, B2A1, A1 ]
- RBAC1 General role hierarchies supports multiple inheritance, from two or more parents.
- e.g. Assignees with role *D4C2B1A1*: [ C3B2A1, C2B1A1,  B2A1, B1A1, A1 ]


### Sample Hierarchy - Role: Parent(s)

```
A1
B1A1: A1
B2A1: A1
C1B1A1: A1, B1A1
C2B1A1: A1, B1A1
C3B2A1: A1, B2A1
C4B2A1: A1, B2A1
D1C1B1A1: A1, B1A1, C1B1A1 
D2C1B1A1: A1, B1A1, C1B1A1
D3C2B1A1: A1, B1A1, C2B1A1
D4C2B1A1: A1, B1A1, C2B1A1
D5C3B2A1: A1, B2A1, C3B2A1
D6C3B2A1: A1, B2A1, C3B2A1
D7C4B2A1: A1, B2A1, C4B2A1
D8C4B2A1: A1, B2A1, C4B2A1
```
