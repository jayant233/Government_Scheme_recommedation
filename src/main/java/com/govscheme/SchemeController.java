package com.govscheme;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;

@Controller
public class SchemeController {

    // We reuse your existing DAO directly - no need for complex Spring Data JPA!
    private SchemeDAO dao = new SchemeDAO();

    @GetMapping("/")
    public String home(Model model) {
        populateDropdowns(model);
        return "index"; // Looks for src/main/resources/templates/index.html
    }

    @GetMapping("/results")
    public String results(
            @RequestParam("age") int age,
            @RequestParam("income") double income,
            @RequestParam(value = "gender", defaultValue = "") String gender,
            @RequestParam(value = "category", defaultValue = "") String category,
            @RequestParam(value = "occupation", defaultValue = "") String occupation,
            @RequestParam(value = "state", defaultValue = "") String state,
            Model model) {
        
        populateDropdowns(model);
        try {
            ArrayList<Scheme> schemes = dao.getEligibleSchemes(age, income, gender, category, occupation, state);
            model.addAttribute("eligibleSchemes", schemes);
        } catch (Exception e) {
            model.addAttribute("error", "Database connection failed.");
            model.addAttribute("eligibleSchemes", new ArrayList<>());
        }
        return "index";
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        populateDropdowns(model);
        try {
            model.addAttribute("schemes", dao.getAllSchemes());
        } catch (Exception e) {
            model.addAttribute("error", "Database connection failed.");
            model.addAttribute("schemes", new ArrayList<>());
        }
        return "admin";
    }

    private void populateDropdowns(Model model) {
        try {
            model.addAttribute("genders", dao.getGenders());
            model.addAttribute("occupations", dao.getOccupations());
            model.addAttribute("categories", dao.getCategories());
            model.addAttribute("states", dao.getStates());
        } catch (Exception e) {
            System.out.println("Could not load dropdown options from database.");
        }
    }

    @PostMapping("/admin/add")
    public String addScheme(
            @RequestParam("name") String name,
            @RequestParam("minAge") int minAge,
            @RequestParam("maxAge") int maxAge,
            @RequestParam("minIncome") double minIncome,
            @RequestParam("maxIncome") double maxIncome,
            @RequestParam("gender") String gender,
            @RequestParam("occupation") String occupation,
            @RequestParam("category") String category,
            @RequestParam("state") String state,
            @RequestParam("desc") String desc,
            RedirectAttributes redirectAttributes) {
        
        try {
            Scheme scheme = new Scheme(0, name, minAge, maxAge, minIncome, maxIncome, gender, occupation, category, state, desc);
            dao.addScheme(scheme);
            redirectAttributes.addFlashAttribute("success", "Scheme '" + name + "' added successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Failed to add scheme: " + e.getMessage());
        }
        return "redirect:/admin";
    }

    @PostMapping("/admin/update")
    public String updateScheme(
            @RequestParam("id") int id, 
            @RequestParam("name") String name,
            @RequestParam("minAge") int minAge,
            @RequestParam("maxAge") int maxAge,
            @RequestParam("minIncome") double minIncome,
            @RequestParam("maxIncome") double maxIncome,
            @RequestParam("gender") String gender,
            @RequestParam("occupation") String occupation,
            @RequestParam("category") String category,
            @RequestParam("state") String state,
            @RequestParam("desc") String desc,
            RedirectAttributes redirectAttributes) {
        try {
            Scheme scheme = new Scheme(id, name, minAge, maxAge, minIncome, maxIncome, gender, occupation, category, state, desc);
            boolean updated = dao.updateScheme(scheme);
            if (updated) {
                redirectAttributes.addFlashAttribute("success", "Scheme ID " + id + " updated successfully.");
            } else {
                redirectAttributes.addFlashAttribute("error", "Scheme ID " + id + " not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Failed to update scheme: " + e.getMessage());
        }
        return "redirect:/admin";
    }

    @PostMapping("/admin/delete")
    public String deleteScheme(@RequestParam("id") int id,
            RedirectAttributes redirectAttributes) {
        try {
            boolean deleted = dao.deleteScheme(id);
            if (deleted) {
                redirectAttributes.addFlashAttribute("success", "Scheme ID " + id + " deleted successfully.");
            } else {
                redirectAttributes.addFlashAttribute("error", "Scheme ID " + id + " not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Failed to delete scheme: " + e.getMessage());
        }
        return "redirect:/admin";
    }
}
