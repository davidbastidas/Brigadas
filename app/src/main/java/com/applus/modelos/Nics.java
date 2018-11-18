package com.applus.modelos;

public class Nics {
    private long id;
    private long nic;
    private long fkBarrio;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getNic() {
        return nic;
    }

    public void setNic(long nic) {
        this.nic = nic;
    }

    public long getFkBarrio() {
        return fkBarrio;
    }

    public void setFkBarrio(long fkBarrio) {
        this.fkBarrio = fkBarrio;
    }
}

