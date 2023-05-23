mod sowolver;
mod ig;

use std::{fs::File, io::{BufRead,BufReader}};

use crate::sowolver::SOwOlver;

fn main() {
    let file = File::open("formula.cnf").expect("cannot read file");
    let mut file = BufReader::new(file);
    let mut first_line = String::new();
    let _ = file.read_line(&mut first_line);
    let mut varCount = 0;
    let mut clauseCount = 0;
    for (idx,val) in first_line.split_ascii_whitespace().enumerate(){
        if idx == 2 {
            varCount = val.parse::<usize>().expect("file malformed");
        }
        if idx == 3 {
            clauseCount = val.parse::<usize>().expect("file malformed")+3;
        }
    }
    let mut s = SOwOlver::new(varCount, clauseCount);


        for line in file.lines().filter_map(|result| result.ok()){
            for val in line.split_ascii_whitespace() {
                let lit = val.parse::<i128>().expect("file malformed");
                s.add(lit.abs().try_into().unwrap(),lit >0);
            }
            
    
    }
    s.add(1, true);
    s.add(2, true);
    s.add(3, true);
    s.add(4, true);
    s.add(5, true);
    s.add(0, false);
    s.add(1, true);
    s.add(2, true);
    s.add(3, true);
    s.add(4, true);
    s.add(5, false);
    s.add(0, false);
    s.add(1, true);
    s.add(2, true);
    s.add(3, false);
    s.add(4, true);
    s.add(5, false);
    s.add(0, false);
    s.add(1, false);
    s.add(2, false);
    s.add(3, false);
    s.add(4, false);
    s.add(5, false);
    s.add(0, false);
    println!("{:?}", s.clauses);
    println!("{}", s.solve());
    println!("{:?}", s.vars);
    println!("{:?}", s.clauses);
    println!("{}", s.clauses.len());
    println!("{:?}", s.ig);
    print_assignment(s);
}

fn print_assignment(s: SOwOlver) {
    for var in s.vars {
        println!("{} {:?}", var.n, var.ass);
    }
}
