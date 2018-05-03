package controller;


import org.springframework.web.bind.annotation.PathVariable;

public interface ISyncController
{
    void generateSync(@PathVariable("size") String size);
}
