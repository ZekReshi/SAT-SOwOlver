use std::collections::HashMap;

#[repr(u8)]
#[derive(Debug, PartialEq, Eq)]
pub enum Ass {
    False,
    True,
    Unass
}

impl Ass {
    fn neg(&self) -> Ass {
        match self {
            Ass::False => Ass::True,
            Ass::True => Ass::False,
            Ass::Unass => Ass::Unass
        }
    }
}

#[derive(Debug)]
pub struct Var {
    pub ass: Ass,
    watched_true: Vec<usize>,
    watched_false: Vec<usize>,
    pub n: usize
}

impl Var {
    pub fn new(n: usize) -> Self {
        Self {
            ass: Ass::Unass,
            watched_true: Vec::new(),
            watched_false: Vec::new(),
            n
        }
    }
}

#[derive(Debug)]
pub struct Clause {
    lits: HashMap<usize, bool>,
    watched: [usize;2],
    n: usize
}

impl Clause {
    pub fn new(n: usize) -> Self {
        Self {
            lits: HashMap::new(),
            watched: [0, 0],
            n
        }
    }
}

pub struct SOwOlver {
    pub num_vars: usize,
    pub num_clauses: usize,
    pub vars: Vec<Var>,
    pub clauses: Vec<Clause>,
    ass_queue: Vec<(usize, bool)>
}

impl SOwOlver {
    pub fn signature(&self) -> &'static str {
        "Version 1"
    }

    pub fn new(num_vars: usize, num_clauses: usize) -> Self {
        let mut s = Self {
            num_vars,
            num_clauses,
            vars: Vec::with_capacity(num_vars),
            clauses: Vec::with_capacity(num_clauses),
            ass_queue: Vec::new()
        };
        for i in 0..num_vars {
            s.vars.push(Var::new(i + 1));
        }
        s.clauses.push(Clause::new(s.clauses.len() + 1));
        s
    }

    pub fn add(&mut self, var: usize, sign: bool) {
        if var == 0 {
            if self.clauses.last().unwrap().lits.len() == 1 {
                let var = self.clauses.last().unwrap().lits.keys().last().unwrap();
                self.ass_queue.push((*var, *self.clauses.last().unwrap().lits.get(var).unwrap()));
            }
            if self.clauses.len() < self.num_clauses {
                self.clauses.push(Clause::new(self.clauses.len() + 1));
            }
        }
        else {
            let clause = self.clauses.last_mut().unwrap();
            clause.lits.insert(var, sign);
            let v = self.vars.get_mut(var - 1).unwrap();
            let mut watched = false;
            if clause.watched[0] == 0 {
                clause.watched[0] = var;
                watched = true;
            }
            else if clause.watched[1] == 0 {
                clause.watched[1] = var;
                watched = true;
            }
            if watched {
                (*(if sign { &mut v.watched_true } else { &mut v.watched_false })).push(clause.n);
            }
        }
    }

    pub fn solve(&mut self) -> bool {
        self.dpll()
    }

    pub fn failed(&mut self, lit: Var) -> bool {
        todo!()
    }

    pub fn dpll(&mut self) -> bool {
        self.bcp();
        let decision = self.dlis();
        if decision == 0 { return true }
        self.ass_queue.push((decision, false));
        if self.dpll() {
            return true;
        }
        self.ass_queue.push((decision, true));
        self.dpll()
    }

    fn bcp(&mut self) -> bool {
        println!("Assignment Queue: {:?}", self.ass_queue);
        while let Some(pending_ass) = self.ass_queue.pop() {
            println!("Assigning {} {}", pending_ass.0, pending_ass.1);
            let var: &mut Var = self.vars.get_mut(pending_ass.0 - 1).unwrap();
            if pending_ass.1 && var.ass == Ass::False || !pending_ass.1 && var.ass == Ass::True { return false; }
            var.ass = if pending_ass.1 { Ass::True } else { Ass::False };

            let var = self.vars.get(pending_ass.0 - 1).unwrap();
            let clauses = if pending_ass.1 { &var.watched_false } else { &var.watched_true };
            for clause in clauses {
                let clause = self.clauses.get_mut(clause - 1).unwrap();
                if clause.watched[0] != pending_ass.0 {
                    if clause.watched[1] == pending_ass.0 {
                        clause.watched.swap(0, 1);
                    }
                    else {
                        continue;
                    }
                }
                let mut lit_found = false;
                for lit in &clause.lits {
                    if *lit.0 != clause.watched[1] && (self.vars.get(*lit.0 - 1).unwrap().ass == Ass::Unass ||
                        (*lit.1 && self.vars.get(lit.0 - 1).unwrap().ass == Ass::True || 
                        !*lit.1 && self.vars.get(lit.0 - 1).unwrap().ass == Ass::False)) {
                        println!("Relinking watch pointer in clause {}: {} -> {}", clause.n, clause.watched[0], *lit.0);
                        clause.watched[0] = *lit.0;
                        lit_found = true;
                        break;
                    }
                }
                if !lit_found {
                    clause.watched[0] = 0;
                    if clause.watched[1] == 0 {
                        return false;
                    }
                    else {
                        let ass = *clause.lits.get(&clause.watched[1]).unwrap();
                        println!("Unit Clause detected: {}, queueing {} {}", clause.n, clause.watched[1], ass);
                        self.ass_queue.push((clause.watched[1], ass));
                    }
                }
            }
        }
        true
    }

    fn dlis(&self) -> usize {
        for var in &self.vars {
            if var.ass == Ass::Unass {
                println!("Deciding {}", var.n);
                return var.n;
            }
        }
        0
    }
}
