class AddWordIdToDraws < ActiveRecord::Migration
  def change
    add_column :draws, :word_id, :integer
  end
end
