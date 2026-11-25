package med.voll.api.validator;

import lombok.RequiredArgsConstructor;
import med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import med.voll.api.exception.ValidacaoException;
import med.voll.api.repository.MedicoRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MedicoAtivoValidator implements ConsultaValidator{
    private final MedicoRepository medicoRepository;

    public void validar(DadosAgendamentoConsulta dados){
        //escolha do médico opcional
        if(dados.idMedico() == null){
            return;
        }

        var medicoEstaAtivo = medicoRepository.findAtivoById(dados.idMedico());
        if(!medicoEstaAtivo){
            throw new ValidacaoException("Consulta não pode ser agendada com médico inativo.");
        }
    }
}
