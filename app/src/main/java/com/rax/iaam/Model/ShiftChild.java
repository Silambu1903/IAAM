package com.rax.iaam.Model;

public class ShiftChild {



        int id,parent_hierarchy_id;
        String name;
        private boolean childCheck;

        public ShiftChild(int id, String name,int parent_hierarchy_id) {
            this.id = id;
            this.name = name;
            this.parent_hierarchy_id=parent_hierarchy_id;
        }

    public int getParent_hierarchy_id() {
        return parent_hierarchy_id;
    }

    public void setParent_hierarchy_id(int parent_hierarchy_id) {
        this.parent_hierarchy_id = parent_hierarchy_id;
    }

    public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isChildCheck() {
            return childCheck;
        }

        public void setChildCheck(boolean childCheck) {
            this.childCheck = childCheck;
        }
    }


