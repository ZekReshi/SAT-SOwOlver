const FALSE: u8 = 0;
const TRUE: u8 = 1;
const UNASSIGNED: u8 = 3;

const UNDECIDED: u8 = 0;
const SATISFIED: u8 = 1;
const UNSATISFIED: u8 = 2;
const UNIT: u8 = 3;

#[derive(Debug)]
pub struct Var {
    val: u8,
    watched_in: Vec<Clause>
}

impl Var {
    pub fn new() -> Self {
        Self {
            val: UNASSIGNED,
            watched_in: Vec::new()
        }
    }
}

#[derive(Debug)]
pub struct Clause {
    literals: Vec<i128>,
    status: u8,
    watched: Vec<u128>
}

impl Clause {
    pub fn new() -> Self {
        Self {
            literals: Vec::new(),
            status: UNDECIDED,
            watched: Vec::new()
        }
    }
}

pub struct SOwOlver {
    pub num_vars: usize,
    pub num_clauses: usize,
    pub vars: Vec<Var>,
    pub clauses: Vec<Clause>
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
            clauses: Vec::with_capacity(num_clauses)
        };
        for _ in 0..num_vars {
            s.vars.push(Var::new());
        }
        s.clauses.push(Clause::new());
        s
    }

    pub fn add(&mut self, lit: i128) {
        if lit == 0 && self.clauses.len() < self.num_clauses {
            self.clauses.push(Clause::new());
        }
        else {
            let c = self.clauses.last_mut().unwrap();
            c.literals.push(lit);
            if c.watched.len() < 2 {
                c.watched.push(lit.unsigned_abs());
            }
        }
    }

    pub fn assume(&mut self, lit: i128) {
        let lit_obj = self.get_literal_mut(lit.unsigned_abs());
        lit_obj.val = u8::from(lit > 0);
    }

    pub fn solve(&mut self) -> bool {
        self.dpll()
    }

    pub fn val(&mut self, lit: i128) -> u8 {
        self.get_literal_mut(lit.unsigned_abs()).val
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
        for clause in &self.clauses {
            match clause.status {
                UNDECIDED | UNIT => sat = false,
                UNSATISFIED => return false,
                SATISFIED => (),
                _ => panic!("Invalid Status"),
            }
        }
        if sat {
            return sat;
        }
        let picked_var = self.dlis();
        self.get_literal_mut(picked_var).val = TRUE;
        if self.dpll() {
            return true;
        }
        self.get_literal_mut(picked_var).val = FALSE;
        if self.dpll() {
            true
        }
        else {
            self.get_literal_mut(picked_var).val = UNASSIGNED;
            false
        }
    }

    fn bcp(&mut self) {

    }

    fn dlis(&self) -> u128 {
        1
    }

    fn get_literal_mut(&mut self, lit: u128) -> &mut Var {
        self.vars.get_mut((lit-1) as usize).unwrap()
    }
}
