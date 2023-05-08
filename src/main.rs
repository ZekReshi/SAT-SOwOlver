mod sowolver;

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
            clauseCount = val.parse::<usize>().expect("file malformed");
        }
    }
    let mut s = SOwOlver::new(varCount, clauseCount);


        for line in file.lines().filter_map(|result| result.ok()){
            for val in line.split_ascii_whitespace() {
                let lit = val.parse::<i128>().expect("file malformed");
                s.add(lit.abs().try_into().unwrap(),lit >0);
            }
            
    
    }
    println!("{:?}", s.vars);
    println!("{:?}", s.clauses.len());
}
