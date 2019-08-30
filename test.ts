interface A {
    a: number | null | undefined
}

interface B extends A {
    a: number | null
}

interface C extends A {
    a: number | undefined
}

interface D extends B, C {
    a: number
}

const foo: D = {
    a: 1
}

const fooA: A = foo;
