package fr.yncrea.cir3.minesweeper.controller;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.yncrea.cir3.minesweeper.form.ModeForm;
import fr.yncrea.cir3.minesweeper.model.Mode;
import fr.yncrea.cir3.minesweeper.repository.GameRepository;
import fr.yncrea.cir3.minesweeper.repository.ModeRepository;

@Controller
@RequestMapping("/mode")
public class ModeController {
	@Autowired
	private ModeRepository modes;
	
	@Autowired
	private GameRepository games;
	
	@GetMapping
	public String list(Model model) {
		model.addAttribute("modes", modes.findAll());
		
		return "mode/list";
	}
	
	@GetMapping({"/add", "edit/{id}"})
	public String add(@PathVariable(required=false) Long id, Model model) {
		ModeForm form = new ModeForm();
		model.addAttribute("mode", form);
		
		if (id != null) {
			Mode c = modes.findById(id).get();
			
			form.setId(c.getId());
			form.setLabel(c.getLabel());
			form.setWidth(c.getWidth());
			form.setHeight(c.getHeight());
			form.setMines(c.getMines());
		}

		return "mode/edit";
	}
	
	@Transactional
	@PostMapping("/add")
	public String addForm(@Valid @ModelAttribute("mode") ModeForm form, BindingResult result, Model model) {
		if (result.hasErrors()) {
			model.addAttribute("mode", form);
			return "mode/edit";
		}
		
		Mode c = new Mode();
		if (form.getId() != null) {
			c = modes.findById(form.getId()).get();
			games.deleteByMode(c);
		}
		
		c.setLabel(form.getLabel());
		c.setWidth(form.getWidth());
		c.setHeight(form.getHeight());
		c.setMines(form.getMines());
		
		modes.save(c);

		return "redirect:/mode";
	}
	
	@Transactional
	@GetMapping("/delete/{id}")
	public String remove(@PathVariable Long id) {
		Mode c = modes.findById(id).get();
		games.deleteByMode(c);
		modes.delete(c);
		
		return "redirect:/mode";
	}
}
