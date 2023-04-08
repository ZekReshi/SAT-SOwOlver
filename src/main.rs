mod sowolver;

use crate::sowolver::SOwOlver;

fn main() {
    let mut s = SOwOlver::new(5, 3);
    s.add(1);
    s.add(-2);
    s.add(0);
    s.add(4);
    s.add(-5);
    s.add(0);
    s.add(2);
    s.add(-3);
    s.add(4);
    s.add(0);
    s.assume(1);
    s.assume(-5);
    println!("{:?}", s.vars);
    println!("{:?}", s.clauses);
}
