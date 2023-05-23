use std::collections::HashMap;

use crate::sowolver::Clause;

#[derive(Debug)]
pub struct Node {
    var: usize,
    level: usize,
    implication: bool,
    reason: Vec<usize>,
    clause: usize
}

impl Node {
    pub fn new(var: usize, level: usize, implication: bool, clause: usize) -> Self {
        Self {
            var,
            level,
            implication,
            reason: Vec::new(),
            clause
        }
    }
}

#[derive(Debug)]
pub struct IG {
    nodes: Vec<Node>,
    indices: HashMap<usize, usize>,
    decisions: Vec<usize>
}

impl IG {
    pub fn new() -> Self {
        Self {
            nodes: Vec::new(),
            indices: HashMap::new(),
            decisions: Vec::new()
        }
    }

    pub fn decide(&mut self, var: usize) {
        if var == 0 {
            println!("decide 0");
        }
        let node = Node::new(
            var, 
            self.decisions.len() + 1, 
            false, 
            0
        );
        self.decisions.push(self.nodes.len());
        self.indices.insert(var, self.nodes.len());
        self.nodes.push(node);
    }
    
    pub fn imply(&mut self, var: usize, clause: &Clause) {
        if var == 0 {
            println!("imply 0");
        }
        let mut node = Node::new(
            var, 
            self.decisions.len() + 1, 
            true, 
            clause.n
        );
        for c_var in clause.lits.keys() {
            if *c_var != var {
                node.reason.push(*self.indices.get(c_var).unwrap());
            }
        }
        self.indices.insert(var, self.nodes.len());
        self.nodes.push(node);
    }

    pub fn conflict(&mut self) -> Vec<usize> {
        let mut vars: Vec<usize> = Vec::new();
        while let Some(node) = self.nodes.pop() {
            vars.push(node.var);
            if !node.implication {
                self.decisions.pop();
                return vars;
            }
        }
        println!("{:?}", vars);
        return vars;
    }
}
