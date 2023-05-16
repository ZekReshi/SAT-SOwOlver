use std::collections::HashMap;

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
    
    pub fn imply(&mut self, var: usize, reason: Vec<usize>, clause: usize) {
        let mut node = Node::new(
            var, 
            self.decisions.len() + 1, 
            true, 
            clause
        );
        for r in reason {
            node.reason.push(*self.indices.get(&r).unwrap());
        }
        self.decisions.push(self.nodes.len());
        self.indices.insert(var, self.nodes.len());
        self.nodes.push(node);
    }
}
