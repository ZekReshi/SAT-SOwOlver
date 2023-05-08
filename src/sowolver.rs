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
    ass: Ass,
    watched_true: Vec<Clause>,
    watched_false: Vec<Clause>
}

impl Var {
    pub fn new() -> Self {
        Self {
            ass: Ass::Unass,
            watched_true: Vec::new(),
            watched_false: Vec::new()
        }
    }
}

#[derive(Debug)]
pub struct Clause {
    lits: HashMap<usize, bool>,
    watched: [usize;2]
}

impl Clause {
    pub fn new() -> Self {
        Self {
            lits: HashMap::new(),
            watched: [0, 0]
        }
    }
}

pub struct SOwOlver {
    pub num_vars: usize,
    pub num_clauses: usize,
    pub vars: Vec<Var>,
    pub clauses: Vec<Clause>,
    ass_queue: HashMap<usize, bool>
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
            ass_queue: HashMap::new()
        };
        for _ in 0..num_vars {
            s.vars.push(Var::new());
        }
        s.clauses.push(Clause::new());
        s
    }

    pub fn add(&mut self, var: usize, sign: bool) {
        if var == 0 {
            if self.clauses.len() == 1 {
                let var = self.clauses.last().unwrap().lits.keys().last().unwrap();
                self.ass_queue.insert(*var, *self.clauses.last().unwrap().lits.get(var).unwrap());
            }
            if self.clauses.len() < self.num_clauses {
                self.clauses.push(Clause::new());
            }
        }
        else {
            let c = self.clauses.last_mut().unwrap();
            c.lits.insert(var, sign);
            if c.watched[0] == 0 {
                c.watched[0] = var;
            }
            else if c.watched[1] == 0 {
                c.watched[1] = var;
            }
        }
    }

    pub fn assume(&mut self, lit: i128) {
        //let lit_obj = self.get_var_mut(lit.unsigned_abs());
        //lit_obj.val = if lit > 0 { Val::True } else { Val::False };
    }

    pub fn solve(&mut self) -> bool {
        self.dpll()
    }

    pub fn failed(&mut self, lit: Var) -> bool {
        todo!()
    }

    pub fn set_terminate<F>(&mut self, callback: F)
    where
        F: FnMut() {
        todo!()
    }

    pub fn dpll(&mut self) -> bool {
        self.bcp();
        let mut sat = true;
        if sat {
            return sat;
        }
        let decision = self.dlis();
        self.ass_queue.insert(decision, true);
        if self.dpll() {
            return true;
        }
        self.ass_queue.insert(decision, false);
        return self.dpll();
    }

    fn bcp(&mut self) -> Ass {
        for pending_ass in self.ass_queue.iter() {
            let var = self.vars.get_mut(*pending_ass.0).unwrap();
            if var.ass != Ass::Unass { return Ass::Unass; }
            var.ass = if *pending_ass.1 { Ass::True } else { Ass::False };

            let clauses = if *pending_ass.1 { &var.watched_false } else { &var.watched_true };
            for clause in clauses {

            }
        }
        return Ass::Unass;
        // TODO
    }

    fn dlis(&self) -> usize {
        1
    }
}
