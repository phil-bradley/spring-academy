/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ie.philb.springacademy;

import org.springframework.data.annotation.Id;

/**
 *
 * @author Phil
 */
public record CashCard (@Id Long id, Double amount) {
    
}
